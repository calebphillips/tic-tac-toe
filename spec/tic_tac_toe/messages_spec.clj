(ns tic-tac-toe.messages-spec
  (:use [speclj.core]
        [tic-tac-toe.messages]))

(describe
  "Formatting a space on the board for display"
  (it "returns spaces for a nil spot on the board"
      (should= "   " (format-space nil)))
  
  (it "returns an uppercase character for a keyword"
      (should= " X " (format-space :x))
      (should= " O " (format-space :o))))

(describe
  "Formatting the board"
  (it "represents the current state"
      (should= "XXXOXOOOO"
               (apply str 
                      (filter #{\X \O} (format-board [:x :x :x :o :x :o :o :o :o]))))))
