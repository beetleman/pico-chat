(ns pico-chat.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [pico-chat.core-test]))

(doo-tests 'pico-chat.core-test)

