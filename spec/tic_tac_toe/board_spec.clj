(ns tic-tac-toe.board-spec
  (:use [speclj.core]
        [tic-tac-toe.board]))

(describe
  "Dividing the board into rows"
  (it "returns three seqs"
      (should= [[:x :x :x] [:o :o :o] [nil nil nil]]
               (rows [:x :x :x :o :o :o nil nil nil]))))

(describe
  "Dividing the board into columns"
  (it "returns three seqs"
      (should= [[:x :o nil] [:x :o nil] [:x :o nil]]
               (columns [:x :x :x :o :o :o nil nil nil]))))

(describe 
  "Checking for a winner"
  (it "detects horizontal wins"
      (should= :x (winner [:x :x :x,
                           :x :o :o,
                           :o :o :x]))

      (should= :x (winner [:o :x :o,
                           :x :x :x,
                           :o :o :x]))

      (should= :x (winner [:o :x :o,
                           :o :o :x,
                           :x :x :x]))

      (should= :o (winner [:o :o :o,
                           :x :o :x,
                           :x :x :o]))

      (should= :o (winner [:x :o :x,
                           :o :o :o,
                           :x :x :o]))

      (should= :o (winner [:x :o :x,
                           :x :x :o,
                           :o :o :o])))

  (it "detects vertical wins"
      (should= :x (winner [:x :o :x,
                           :x :o :o,
                           :x :x :o]))

      (should= :x (winner [:o :x :o,
                           :x :x :o,
                           :o :x :x]))

      (should= :x (winner [:o :x :x,
                           :o :o :x,
                           :x :o :x]))

      (should= :o (winner [:o :x :o,
                           :o :x :x,
                           :o :o :x]))

      (should= :o (winner [:x :o :x,
                           :o :o :x,
                           :x :o :o]))

      (should= :o (winner [:x :o :o,
                           :x :x :o,
                           :o :x :o])))

  (it "detects diagonal wins"
      (should= :x (winner [:x :o :o
                           :o :x :o
                           :o :o :x]))

      (should= :o (winner [:x :x :o
                           :x :o :x
                           :o :x :x])))

  (it "detects wins when the board is not full"
      (should= :o (winner [nil nil nil
                           :o :o :o
                           :x :x nil]))
      (should= :x (winner [nil :x :o
                           nil :x nil
                           nil :x :o]))
      (should= :x (winner [:x nil :o
                           nil :x nil
                           :o :o :x])))

  (it "returns nil when there is no winner yet"
      (should= nil (winner (repeat 9 nil)))

      (should= nil (winner [:x nil :X
                            :o :o nil
                            :x nil :o]))

      (should= nil (winner [:x :o :o
                            :o :x :x
                            :x :o :o]))))

(describe
  "Finding empty cells"
  (it "returns a list of indexes"
      (should= [0 1 2 3 4 5 6 7 8]
               (empty-cells [nil nil nil nil nil nil nil nil nil]))
      (should= [1 3 5 7]
               (empty-cells [:x nil :x nil :x nil :x nil :x]))))

(describe 
  "Checking for a tie"
  (it "detects a tie when the board is full"
      (should (tie? [:x :o :x
                     :o :x :o
                     :o :x :o])))

  (it "returns nil for a full board with a win"
      (should-not (tie? [:x :x :o
                         :o :x :o
                         :o :o :x]))))

(describe 
  "Checking for remaining moves"
  (it "returns falsey when there is a winner"
      (should-not (moves-remaining? [:x :x :x :o :nil :o nil :o nil])))

  (it "returns falsey when the board is full"
      (should-not (moves-remaining? [:x :o :x
                                     :o :x :o
                                     :o :x :o])))
  (it "returns truthy when there are more moves"
      (should (moves-remaining? [:x :o nil nil nil nil nil nil nil]))))

(describe
  "valid-move?"
  (it "is false if the move is out of bounds"
      (should-not (valid-move? (init-board) -100))
      (should-not (valid-move? (init-board) -1))
      (should-not (valid-move? (init-board) 9))
      (should-not (valid-move? (init-board) 100)))

  (it "is false if the spot is already taken"
      (should-not (valid-move? [nil nil nil nil :x nil nil nil nil] 4)))

  (it "is true is the spot is available"
      (doseq [i (range 9)]
        (should (valid-move? (init-board) i))))) 

(describe
  "all-moves"
  (it "returns a list of boards and moves"
      (let [moves (all-moves (init-board) :x)]
        (should= [[:x nil nil nil nil nil nil nil nil] 0] (first moves))
        (should= [[nil nil nil nil nil nil nil nil :x] 8] (last moves)))))

(describe 
  "Moving a player"
  (it "updates the board with the move"
      (should= [nil :x nil nil nil nil nil nil :o]
               (move-player [nil :x nil nil nil nil nil nil nil] 8 :o)))

  (it "throws an exception if the space is already taken"
      (should-throw IllegalArgumentException "That space is already taken."
                    (move-player [:x nil nil nil nil nil nil nil nil] 0 :o))))

(describe
  "Determining the winning sequence"
  (it "returns the correct value for row wins"
      (should= [0 1 2] 
               (winning-seq [:x :x :x 
                             :o :o nil 
                             nil nil nil]))
      (should= [3 4 5] 
               (winning-seq [:o :x :x 
                             :o :o :o 
                             nil :x nil]))
      (should= [6 7 8] 
               (winning-seq [:o :x :x 
                             :o nil nil 
                             :x :x :x])))
  (it "returns the correct value for column wins"
      (should= [0 3 6]
               (winning-seq [:o nil :x
                             :o :x nil
                             :o nil nil])))
  (it "returns the correct value for diagonal wins"
      (should= [6 4 2]
               (winning-seq [nil nil :x
                             nil :x nil
                             :x nil nil])))
  )
