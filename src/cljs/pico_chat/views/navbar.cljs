(ns pico-chat.views.navbar
  (:require [reagent.core :as r]
            [reagent.session :as session]))


(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "pico-chat"]
        [:ul.nav.navbar-nav
         [nav-link "#/doc" "Doc" :doc collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]]])))
