(ns pico-chat.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [re-frame.core :refer [dispatch-sync]]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [pico-chat.subs]
            [pico-chat.handlers]
            [pico-chat.websocket]
            [pico-chat.ajax :refer [load-interceptors!]]
            [pico-chat.views.navbar :refer [navbar]]
            [pico-chat.views.about :as about]
            [pico-chat.views.home :as home]
            [pico-chat.views.doc :as doc]
            [mount.core :as mount]
            [ajax.core :refer [GET POST]])
  (:import goog.History))


(def pages
  {:home #'home/page
   :doc #'doc/page
   :about #'about/page})

(defn page []
  [(pages (session/get :page))])


;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/doc" []
  (session/put! :page :doc))

(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! [] ;TODO: use mount here
  (load-interceptors!)
  (hook-browser-navigation!)
  (dispatch-sync [:initialise-db])
  (mount/start)
  (mount-components))
