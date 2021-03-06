(ns pico-chat.handlers
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :refer [register-handler
                                   path
                                   trim-v
                                   after
                                   debug
                                   dispatch]]
            [pico-chat.logger :as logger]
            [pico-chat.websocket :refer [sch]]
            [pico-chat.db :as d]))


;; -- Helpers -----------------------------------------------------------------


;; -- Event Handlers ----------------------------------------------------------

(register-handler
 :initialise-db
 (fn [_ _]
   d/default-value))  ;; all hail the new state


;; -- doc
(register-handler
 :doc-request
 (fn [db _]
   (GET
    (str js/context "/docs")
    {:handler       #(dispatch [:doc-response %1])
     :error-handler #(dispatch [:doc-response-error %1])})
   (assoc db :loading? true)))

(register-handler
 :doc-response
 (fn [db [_ response]]
   (assoc db :doc response)))

(register-handler
 :doc-response-error
 (fn [db [_ response]]
   (assoc db :doc "")))


;; -- msg
(register-handler
 :send-message
 trim-v
 (fn
   [db [text]]
   ((:send-fn @sch) [:chat/new-message {:text text}])
   db))

(register-handler
 :recv-message
 trim-v
 (fn
   [db [message]]
   (d/add-message db message)))


(register-handler
 :recv-messages
 trim-v
 (fn
   [db [message]]
   (d/add-messages db message)))


(register-handler
 :recv-users
 trim-v
 (fn
   [db [users]]
   (d/set-users db users)))
