(ns pico-chat.db)

(def default-value
  {:version [0 0 1]
   :doc ""
   :users []
   :messages []})

(defn distinct-by [k s]
  (map (fn [[_ [v]]] v) (group-by k s)))

(defn sanitize [db]
  (update db :messages (comp
                        #(sort-by :created-at %)
                        #(distinct-by :id %))))

(defn add-message [db msg]
  (->
   (update db :messages conj msg)
   sanitize))

(defn add-messages [db msgs]
  (->
   (update db :messages concat msgs)
   sanitize))

(defn set-users [db users]
  (->
   (assoc db :users users)
   sanitize))
