(ns pico-chat.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [pico-chat.handler :refer :all]
            [pico-chat.test.fixtures :refer [db-fixture]]))


(use-fixtures :each db-fixture)


(deftest test-app
  (testing "main route without credential"
    (let [response ((app) (request :get "/"))]
      (is (= 302 (:status response)))
      (is (clojure.string/starts-with?
           (get-in response [:headers "Location"])
           "https://accounts.google.com/o/oauth2/auth"))))

  (testing "logout route"
    (let [response ((app) (request :get "/logout"))]
      (is (= 302 (:status response)))
      (is (=
           "http://localhost/"
           (get-in response [:headers "Location"])))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
