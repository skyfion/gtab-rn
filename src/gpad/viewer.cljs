(ns gpad.viewer
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [clojure.string :as str]))

(defn parse-lyric-line [lyric]
  (let [xs
        (remove str/blank?
                (str/split (str/replace lyric "[" "[%") #"\]|\["))
        ps
        (if (= 1 (count xs))
          xs
          (map
            (fn [item]
              (if (= \% (first item))
                (keyword (str/join (rest item)))
                (if (empty? item) nil item)))
            xs))]
    (if (string? (first ps)) (conj ps nil) ps)))

(defn chord-block [chord text]
  [rn/view
   [rn/text {:style {:color :red :textTransform :capitalize}}
    (if chord chord "")]
   [rn/text {} text]])

(defn lyric-line [line]
  [rn/view {:style {:flex-direction :row}}
   (for [[chord text] (partition 2 line)]
     [chord-block chord text])])

(defn viewer [text]
  [rn/view
   {:style {:padding   20 :padding-top 40 :font-size 20
            :flex-wrap :nowrap}}
   (map (comp lyric-line parse-lyric-line) text)])