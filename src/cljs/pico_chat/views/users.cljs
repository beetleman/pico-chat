(ns pico-chat.users)


(defn one [{:keys [name]}]
  [:div.user name])


(defn all [users]
  [:div.users
   (for [s @users]
     ^{:key (:id s)} [one s])])
