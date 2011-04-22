(ns tic-tac-toe.core)

(defn rows [board]
  "Returns a seq of 3 seqs for the rows of the board"
  (partition 3 board))

(defn columns [board]
  "Returns a seq of 3 seqs for the columns of the board"
  (for [i (range 3)] (map #(nth % i) (partition 3 board))))

(defn diagonals [board]
  "Returns a seq of 2 seqs for the diagonals"
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(defn three-in-a-row [colls]
  "Takes a collection of collections and returns the value of the first"
  "collection, if any that contains three of the same value"
  (->>  (map #(partition-by identity %) colls)
        (apply concat) 
        (remove #(some nil? %))
        (filter #(= 3 (count %)))
        ffirst))

(defn winner [board]
  "Returns the keyword for the winner, if any, otherwise returns nil"
  (three-in-a-row (concat (rows board) (columns board) (diagonals board))))

(defn indexed-board [board]
  (map vector (iterate inc 0) board))

(defn empty-cells [board] 
  (map first (filter #(nil? (second %)) (indexed-board board))))

; TODO always selecting (second) now - does that mean we only need the index?
; or can we move away from the index and just use the board?
(defn find-win-for [board player]
  "Finds winning board states and returns a seq of vectors of (1) the winning state"
  "and (2) the position of the winning move.  This additional detail is"
  "using for blocking a potential win by the opponent"
  (first (filter #(winner (first %)) 
          (map #(vector (assoc-in board [%] player) %) (empty-cells board)))))

(defn find-in [idx-coll position]
  (map second 
       (first
         (for [r idx-coll :when (seq (filter #(= position (first %)) r))] r) )))

(defn lonely-opponent [coll]
  (and (some #{:o} coll) (not-any? #{:x} coll)))

(defn my-row [board position]
  (find-in (rows (indexed-board board)) position))

(defn my-column [board position]
  (find-in (columns (indexed-board board)) position) )

(defn is-squeezed? [board corner]
  (let [my-row (my-row board corner)
        my-col (my-column board corner)]
    (when (and (lonely-opponent my-row) (lonely-opponent my-col)) 
      corner)))

(defn find-squeezed-corners [board]
  (filter #(is-squeezed? board %) (filter #{0 2 6 8} (empty-cells board))))

(defn move-to-win [board]
  (fn [] (second (find-win-for board :x))))

(defn move-to-block [board]
  (fn [] (second (find-win-for board :o))))

(defn move-to-center [board]
  (fn [] (when-not (get-in board [4]) 4)))

(defn prevent-diagonal-trap [board]
      (fn [] (when (= 2 (count (find-squeezed-corners board)))
               (some #{1 3 5 7} (empty-cells board)))))

(defn prevent-corner-trap [board]
  (fn [] (first (find-squeezed-corners board))))

(defn move-to-corner [board]
  (fn [] (some #{0 2 6 8} (empty-cells board))))

(defn move-to-first-empty [board]
  (fn [] (first (empty-cells board))))

(defn find-x-move [board]
  (some #(%) ((juxt move-to-win
                    move-to-block
                    move-to-center 
                    prevent-diagonal-trap
                    prevent-corner-trap
                    move-to-corner 
                    move-to-first-empty) board)))

(defn move-player [board position player]
  (if (get-in board [position])
    (throw (IllegalArgumentException. "That space is already taken."))
    (assoc-in board [position] player)))

(defn move-x [board] 
  "Returns new board with computer's move marked"
  (move-player board (find-x-move board) :x))

(defn move-o [board position]
  (move-player board position :o))

