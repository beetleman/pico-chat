(ns pico-chat.messages
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [pico-chat.logger :as logger]))


(defn new []
  (let [text (r/atom "")]
    (fn []
      [:form.new-message
       {:onSubmit (fn [e]
                    (.preventDefault e)
                    (dispatch [:send-message @text])
                    (reset! text ""))}
       [:div.input-field
        [:input {:value @text
                 :type "text"
                 :placeholder "Message"
                 :onChange #(reset! text (.. % -target -value))}]]
       [:button.btn "Send"]])))



(defn sender-name [{:keys [name link]}]
  [:a.name {:href link
            :target "_blank"}
   name ": "])


(defn sender-avatar [{:keys [picture]}]
  [:img {:src picture
         :style {:width "100%"}}])


(defn one [{:keys [user text id]} on-new-message]
  [:div.message.row
   {:ref on-new-message}
   [:div.text.col.s2
    [sender-avatar user]]
   [:div.text.col.s10
    [sender-name user]
    [:div.text text]]])


(defn all [messages on-new-message]
  [:div.messages
   (for [m @messages]
     ^{:key (:id m)} [one m on-new-message])])
