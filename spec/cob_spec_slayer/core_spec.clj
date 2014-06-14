(ns cob-spec-slayer.core-spec
  (:require [cob-spec-slayer.core :refer :all]
            [clojure.java.io :refer [delete-file]]
            [speclj.core :refer :all]))

(describe "cob spec slayer"

  (it "formats parameter decoder response"
    (let [request {:query-params {"baz" "snazz" "foo" "bar"}}]
      (should= "baz = snazz\nfoo = bar"
        (param-decode-response request)))))
