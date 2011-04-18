(ns tic-tac-toe.core-spec
  (:use [speclj.core]
        [tic-tac-toe.core]))

(describe "Checking for a winner"
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
                                   :o :o :o]))
              )
          
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

          (it "detects when there is no winner yet"
              (should= nil (winner (repeat 9 nil)))
              
              (should= nil (winner [:x :o :o
                                    :o :x :x
                                    :x :o :o])))
          )

