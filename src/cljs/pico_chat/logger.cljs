(ns pico-chat.logger)

(defn debug [key x]
  (-> {key x} clj->js js/console.debug))

(defn info [key x]
  (-> {key x} clj->js js/console.info))

(defn error [key x]
  (-> {key x} clj->js js/console.error))
