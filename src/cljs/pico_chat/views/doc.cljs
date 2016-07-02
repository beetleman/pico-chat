(ns pico-chat.views.doc
  (:require [reagent.core :as r]
            [markdown.core :refer [md->html]]
            [re-frame.core :refer [subscribe dispatch]]))


(defn page []
  (let [doc-html (subscribe [:doc-html])]
    (fn []
      (dispatch [:doc-request])
      [:div.container
       [:div.jumbotron
        [:h1 "Welcome to pico-chat"]
        [:p [:a.btn.btn-primary.btn-lg {:href "https://github.com/beetleman/pico-chat"}
             "Learn more »"]]]
       [:div.row
        [:div.col-md-12
         [:h2 "Welcome to ClojureScript"]]]
       [:div.row
        [:div.col-md-12
         [:div {:dangerouslySetInnerHTML
                {:__html @doc-html}}]]]])))
