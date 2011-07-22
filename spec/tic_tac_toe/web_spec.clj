(ns tic-tac-toe.web-spec
  (:use [speclj.core]
        [tic-tac-toe.web]))

(describe
  "The choose game type page"
  (it "includes the game types"
      (should (re-seq #"Human vs. Computer" (choose-game-type-page)))
      (should (re-seq #"Computer vs. Human" (choose-game-type-page)))
      (should (re-seq #"Computer vs. Computer" (choose-game-type-page)))
      )
  )
