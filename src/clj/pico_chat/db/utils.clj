(ns pico-chat.db.utils
  (:require
   [rethinkdb.query :as r]
   [mount.core :as mount]
   [clj-time.core :as time]
   [clj-time.coerce :as tc]))


(defn db-exist? [conn db-name]
  (some #{db-name} (-> (r/db-list)
                       (r/run conn))))


(defn table-exist? [conn table-name]
  (some #{table-name} (-> (r/table-list)
                          (r/run conn))))


(defn save-item [item conn table-name]
  (-> (r/table table-name)
      (r/insert (assoc item :created-at (tc/to-long (time/now))))
      (r/run conn)))

(defn save-or-update-item [item conn table-name]
  (-> (r/table table-name)
      (r/insert (assoc item :created-at (tc/to-long (time/now)))
                {:conflict :update})
      (r/run conn)))


(defn get-all-items [conn table-name]
  (-> (r/table table-name)
      (r/run conn)))


(defn changesfeed [conn table-name]
  (-> (r/table table-name)
      (r/changes {:include-initial true})
      (r/run conn {:async? true})))


(defmacro def-table [name conn & indexies]
  `(do
     (def ~name ~(str name))
     (mount/defstate ~(symbol (str "setup-" name))
       :start  (when-not (table-exist? ~conn ~(str name))
                 (-> (r/table-create ~(str name))
                     (r/run ~conn))
                 ~@indexies
                 ~@(for [index indexies]
                     `(-> (r/table ~(str name))
                          ~index
                          (r/run ~conn)))))))
