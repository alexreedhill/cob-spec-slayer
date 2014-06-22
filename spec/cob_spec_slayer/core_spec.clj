(ns cob-spec-slayer.core-spec
  (:require [cob-spec-slayer.core :refer :all]
            [lazy-server.file-interactor :refer [directory-contents]]
            [lazy-server.spec-helper :refer [bytes-to-string]]
            [clojure.java.io :refer [delete-file]]
            [speclj.core :refer :all]))

(describe "cob spec slayer"
  (it "formats parameter decoder response"
    (let [request {:query-params {"baz" "snazz" "foo" "bar"}}]
      (should= "baz = snazz\nfoo = bar"
        (param-decode-response request))))

  (it "formats directory links response"
    (with-redefs [directory-contents (fn [_] '("test.txt", "test.gif"))]
      (should= (str "<a href=\"/test.txt\">test.txt</a><br>\n"
                    "<a href=\"/test.gif\">test.gif</a><br>\n")
        (bytes-to-string (directory-response))))))
