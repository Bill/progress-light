(ns progress-light.core
  (:require [clj-progress.core :as progress]
            [clojure.core.async :as async :refer [>!! timeout chan alt!! thread]]))

;; constructor
(defn progress-light []
  (chan))

(defn monitor-progress 
  ([p-light max-ticks]
   "monitor up to max-ticks; inform every 1000 milliseconds (i.e. 1 second)"
   (monitor-progress p-light max-ticks 1000))
  ([p-light max-ticks inform-every]
   "monitior up to max-ticks; inform every progress-every milliseconds"
   (progress/init "Processing" max-ticks)
   (thread (loop [progress 0
                  timer (timeout inform-every)]
             (alt!!
               p-light ([v ch]
                        (condp = v
                          :tick (recur (inc progress) timer)
                          :done (progress/done)))
               timer (do (when (pos? progress) ;; can't tick-to 0
                           (progress/tick-to progress))
                         (recur progress (timeout inform-every))))))))

(defn tick [ p-light & [obj]] (>!! p-light :tick) obj)
(defn done [ p-light & [obj]] (>!! p-light :done) obj)
