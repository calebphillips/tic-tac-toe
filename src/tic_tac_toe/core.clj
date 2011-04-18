(ns tic-tac-toe.core)

(defn rows [board]
  (partition 3 board))

(defn columns [board]
  (for [i (range 3)] (map #(nth % i) (partition 3 board))))

(defn diagonals [board]
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(defn three-in-a-row [colls]
  "Takes a collection of collections and returns the value of the first"
  "collection, if any that contains three of the same value"
  (first 
    (first
      (filter #(= 3 (count %))
              (remove #(some nil? %) 
                      (apply concat (map #(partition-by identity %) colls)))))))

(defn winner [board]
  "Returns the keyword for the winner, if any, otherwise returns nil"
  (three-in-a-row (concat (rows board) (columns board) (diagonals board))))
