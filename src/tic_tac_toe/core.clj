(ns tic-tac-toe.core)

(defn winner [board]
  (second
    (first
      (filter #(= 3 (first %)) 
              (map (juxt count first) 
                   (apply concat (map (partial partition-by identity) board)))))))
