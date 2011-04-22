(ns tic-tac-toe.core)

(def corners #{0 2 6 8})
(def cross-points #{1 3 5 7})

(defn rows [board]
  "Returns a seq of 3 seqs for the rows of the board"
  (partition 3 board))

(defn columns [board]
  "Returns a seq of 3 seqs for the columns of the board"
  (for [i (range 3)] (map #(nth % i) (rows board))))

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

(defn all-moves [board player]
  ; Returns lazy seq of couplets representing all possibles moves and 
  ; resulting boards: [[board1 move1] [board2 move2]]
  (map #(vector (assoc-in board [%] player) %) (empty-cells board)))

(defn find-win-for [board player]
  (second 
    (first 
      (filter #(winner (first %)) (all-moves board player))))) 

(defn lonely-opponent [coll]
  (and (some #{:o} coll) (not-any? #{:x} coll)))

(defn containing-row [board position]
  (nth (rows board) (quot position 3)))

(defn containing-column [board position]
  (nth (columns board) (rem position 3)))

(defn is-squeezed? [board corner]
  (every? lonely-opponent 
          ((juxt containing-row containing-column) board corner)))

(defn find-squeezed-corners [board]
  (filter #(is-squeezed? board %) (filter corners (empty-cells board))))

(defn move-to-win [board]
  (fn [] (find-win-for board :x)))

(defn move-to-block [board]
  (fn [] (find-win-for board :o)))

(defn move-to-center [board]
  (fn [] (when-not (get-in board [4]) 4)))

(defn prevent-diagonal-trap [board]
  (fn [] (when (= 2 (count (find-squeezed-corners board)))
           (some cross-points (empty-cells board)))))

(defn prevent-corner-trap [board]
  (fn [] (first (find-squeezed-corners board))))

(defn move-to-corner [board]
  (fn [] (some corners (empty-cells board))))

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

