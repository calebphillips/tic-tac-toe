(ns tic-tac-toe.core)

(defn rows [board]
  (partition 3 board))

(defn columns [board]
  (for [i (range 3)] (map #(nth % i) (partition 3 board))))

(defn diagonals [board]
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(defn three-in-a-row [colls]
  (first 
    (first
      (filter #(= 3 (count %))
              (apply concat (map (partial partition-by identity) colls))))))

(defn horizontal-winner [board]
  (three-in-a-row (rows board)))

(defn vertical-winner [board]
  (three-in-a-row (columns board)))

(defn diagonal-winner [board]
  (three-in-a-row (diagonals board)))

(defn winner [board]
  "Returns the keyword for the winner, if any, otherwise returns nil"
  (let [hw (horizontal-winner board)
        vw (vertical-winner board)
        dw (diagonal-winner board)]
    (cond hw hw
          vw vw
          dw dw
          :else nil)))
