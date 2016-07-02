(ns pico-chat.handlers
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :refer [register-handler path trim-v after debug dispatch]]
            [pico-chat.logger :as logger]
            [pico-chat.db :refer [default-value]]))


;; -- Helpers -----------------------------------------------------------------


;; -- Event Handlers ----------------------------------------------------------

(register-handler
 :initialise-db
 (fn [_ _]
   default-value))  ;; all hail the new state


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
