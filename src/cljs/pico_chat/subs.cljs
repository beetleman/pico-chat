(ns pico-chat.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [markdown.core :refer [md->html]]))


;; -- Helpers -----------------------------------------------------------------


;; -- Subscription handlers and registration  ---------------------------------

(register-sub
 :version
 (fn [db _]
   (reaction (:version @db))))

(register-sub
 :doc-html
 (fn [db _]
   (reaction (-> @db :doc md->html))))


(register-sub
 :messages
 (fn [db _]
   (reaction (:messages @db))))
