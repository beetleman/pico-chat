(ns pico-chat.db)

(def default-value
  {:version [0 0 1]
   :doc ""
   :users []
   :messages []})


(defn add-message [db msg]
  (update db :messages conj msg))

(defn set-users [db users]
  (assoc db :users users))
