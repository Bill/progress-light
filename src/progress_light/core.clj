(ns progress-light.core
  (:require [clj-progress.core :as progress]
            [clojure.core.async :as async :refer [>!! timeout chan alt!! thread]]))

(def progress-ch
  (chan))

(defn monitor-progress 
  ([max-ticks]
   "monitor up to max-ticks; inform every 1000 milliseconds (i.e. 1 second)"
   (monitor-progress max-ticks 1000))
  ([max-ticks inform-every]
   "monitior up to max-ticks; inform every progress-every milliseconds"
   (progress/init "Processing" max-ticks)
   (thread (loop [progress 0
                  timer (timeout inform-every)]
             (alt!!
               progress-ch ([v ch]
                            (condp = v
                              :tick (recur (inc progress) timer)
                              :done (progress/done)))
               timer (do (when (pos? progress) ;; can't tick-to 0
                           (progress/tick-to progress))
                         (recur progress (timeout inform-every))))))))

(defn tick [& [obj]] (>!! progress-ch :tick) obj)
(defn done [& [obj]] (>!! progress-ch :done) obj)
