(ns tic-tac-toe.core-spec
  (:use [speclj.core]
        [tic-tac-toe.core]))

(describe "Winning"
          (it "detects horizontal wins"
              (should= :x (winner [:x :x :x,
                                   :o :x :o,
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
                                   :o :x :o]))
              )

          ; (it "detects diagonal wins"
              ; (should= :x (winner [:x :o :o
                                   ; :o :x :o
                                   ; :o :o :x]))
              ; )

          )
