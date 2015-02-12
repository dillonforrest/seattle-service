(ns streaker-service.page
  (:require [ring.util.response :as ring-resp]))


(defn index
  [request]
  (ring-resp/response (format "hello world")))


(defn echo [request]
  (ring-resp/response (with-out-str (clojure.pprint/pprint request))))
