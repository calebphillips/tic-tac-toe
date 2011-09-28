(ns tic-tac-toe.board)

(defn init-board []
  (vec (repeat 9 nil)))

(defn rows 
  "Returns a seq of 3 seqs for the rows of the board"
  [board]
  (partition 3 board))

(defn columns 
  "Returns a seq of 3 seqs for the columns of the board"
  [board]
  (for [i (range 3)] (map #(nth % i) (rows board))))

(defn- diagonals 
  "Returns a seq of 2 seqs for the diagonals"
  [board]
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(def wins #{[:x :x :x] [:o :o :o]})

(defn winner 
  "Returns the keyword for the winner, if any, otherwise returns nil"
  [board]
  (first 
    (some wins
          (concat (rows board) (columns board) (diagonals board)))))

(defn indexed-board [board]
  (map vector (iterate inc 0) board))

(defn winning-seq
  [board]
  (let [ib (indexed-board board)
        seqs (concat (rows ib) (columns ib) (diagonals ib))]
    (first
      (for [r seqs :when (wins (map second r))] 
        (map first r)))))

(defn empty-cells [board] 
  (map first (filter #(nil? (second %)) (indexed-board board))))

(defn tie? [board]
  (and (not (winner board)) 
       (empty? (empty-cells board))))

(defn moves-remaining? [board]
  (not (or (winner board)
           (tie? board))))

(defn valid-move? 
  "Returns whether or not this move is valid given the current state
  of the board"
  [board move]
  (and (< -1 move 9)
       (not (board move))))

(defn all-moves 
  "Returns lazy seq of couplets representing all possibles moves and 
  resulting boards: [[board1 move1] [board2 move2]]"
  [board player]
  (map #(vector (assoc-in board [%] player) %) (empty-cells board)))

(defn move-player [board move player]
  (if (board move)
    (js/alert (str "Already have a move in " move))
    (assoc board move player)))
