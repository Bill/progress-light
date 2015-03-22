(ns progress-light.core-test
  (:require [clojure.test :refer :all]
            [progress-light.core :refer :all]
            [clojure.string :as str]))

(defn test-throttling [& {:keys [max-ticks tick-every]}]
  (println "max-ticks" max-ticks "tick-every" tick-every)
  ;; Since we are dealing with timers here, we don't always get exactly
  ;; 2 frames. Allow for up to three frames.
  (is (<
       1
       (-> (with-out-str
             (monitor-progress max-ticks)
             (dotimes [n max-ticks] (tick) (Thread/sleep tick-every))
             (done))
           (str/split #"\r")
           count)
       4)))

(deftest multiple-ticks-per-frame-test
  (testing "about 100 ticks arrive per (1s) frame; about 2 frames over 2 seconds"
    (test-throttling :max-ticks 200 :tick-every 10)))

(deftest multiple-frames-per-tick-test
  (testing "a tick arrives about once every other (1s) frame; about 2 frames over 2 seconds"
    (test-throttling :max-ticks 1 :tick-every 3000)))


