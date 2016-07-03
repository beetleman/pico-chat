(ns pico-chat.websocket
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.immutant :refer (get-sch-adapter)]
            [mount.core :as mount]))

(mount/defstate ^{:on-reload :noop} sch
  :start (sente/make-channel-socket! (get-sch-adapter)))
