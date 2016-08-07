(ns pico-chat.views.navbar
  (:require [reagent.core :as r]
            [reagent.session :as session]))


(defn nav-link [uri title page collapsed?]
  [:li
   {:class (when (= page (session/get :page)) "active")}
   [:a {:href uri} title]])

(defn logout-link []
  [:li [:a {:href "/logout"} "Logout"]])

(defn navbar []
  [:div.navbar-fixed
   [:nav
    [:div.nav-wrapper
     [:a.brand-logo {:href "#/"} "pico-chat"]
     [:ul.right.hide-on-med-and-down
      [nav-link "#/doc" "Doc" :doc]
      [nav-link "#/about" "About" :about]
      [logout-link]]]]])
