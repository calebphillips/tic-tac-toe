(ns tic-tac-toe.core)

(defn rows [board]
  (partition 3 board))

(defn columns [board]
  (for [i (range 3)] (map #(nth % i) (partition 3 board))))


(defn three-in-a-row [colls]
  (first 
    (first
      (filter #(= 3 (count %))
              (apply concat (map (partial partition-by identity) colls))))))

(defn horizontal-winner [board]
  (three-in-a-row (rows board)))

(defn vertical-winner [board]
  (three-in-a-row (columns board)))

(defn winner [board]
  "Returns the keyword for the winner, if any, otherwise returns nil"
  (let [hw (horizontal-winner board)
        vw (vertical-winner board)]
    (if hw hw vw)))
