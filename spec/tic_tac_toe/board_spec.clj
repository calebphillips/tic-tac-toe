(ns tic-tac-toe.board-spec
  (:use [speclj.core]
        [tic-tac-toe.board]))

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

  (it "returns falsey when there is a tie"
      (should-not (moves-remaining? [:x :o :x
                                     :o :x :o
                                     :o :x :o])))
  (it "returns truthy"
      (should (moves-remaining? [:x :o nil nil nil nil nil nil nil]))))

(describe 
  "Moving a player"

  (it "updates the board with the move"
      (should= [nil :x nil nil nil nil nil nil :o]
               (move-player [nil :x nil nil nil nil nil nil nil] 8 :o)))

  (it "throws an exception if the space is already taken"
      (should-throw IllegalArgumentException "That space is already taken."
                    (move-player [:x nil nil nil nil nil nil nil nil] 0 :o))))
  
