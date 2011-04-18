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
                                    :x :o :o]))))

(describe "Making a move" 
          (it "finds empty cells"
              (should= [0 1 2 3 4 5 6 7 8] (empty-cells (repeat 9 nil)))
              (should= [2 3 4 5 6 7 8] (empty-cells (concat [:x :x] (repeat 7 nil))))
              (should= [8] (empty-cells (concat (repeat 8 :x) [nil])))
              (should= [2 4 8] (empty-cells [:x :o nil, 
                                              :o nil :x,
                                              :o :x nil])))

          (it "wins if it can"
              (should= [:x :x :x 
                        :o nil nil 
                        nil nil :o]
                       (move-x [:x :x nil 
                              :o nil nil 
                              nil nil :o]))
              (should= [:x :o nil 
                        :o :x :o 
                        :o :x :x]
                       (move-x [:x :o nil 
                              :o nil :o 
                              :o :x :x])))

          (it "blocks if it needs to"
              (should= [:o :o :x
                        :x nil nil
                        nil :x nil]
                       (move-x [:o :o nil
                              :x nil nil
                              nil :x nil])))

          (it "chooses winning over blocking"
              (should= [nil nil nil 
                        :o :o nil
                        :x :x :x]
                       (move-x [nil nil nil 
                              :o :o nil
                              :x :x nil])))

          (it "makes some move if neither win or block"
              (should (some #(= :x %)
                       (move-x [nil nil nil nil :o nil nil nil nil]))))
          )

