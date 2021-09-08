(ns gpad.viewer
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [clojure.string :as str]))

(defn- block-chords [data]
  (loop [[a b & xs] data
         result []]
    (cond
      (and (keyword? a) (string? b))
      (recur xs (conj result [a b]))

      (not (nil? a))
      (recur (cons b xs) (conj result a))

      :else
      result)))

(defn parse-lyric-line [lyric]
  ; todo remove %
  (let [xs
        (remove str/blank?
                (str/split (str/replace lyric "[" "[%") #"\]|\["))]
    (->> xs
         (mapv
           (fn [item]
             (if (= \% (first item))
               (keyword (str/join (rest item)))
               (if (empty? item) nil item))))
         block-chords)))

(defn chord-block [chord text]
  [rn/view
   [rn/text {:style {:color :red :textTransform :capitalize}}
    (if chord chord "")]
   [rn/text {} text]])

(defn lyric-line [line]
  [rn/view {:style {:flex-direction :row}}
   (for [word line]
     (cond
       (vector? word) (apply chord-block word)
       :else
       [chord-block nil
        (if (keyword? word)
          (str (name word) " ")
          word)]))])

(defn viewer [text]
  [rn/scroll-view
   [rn/view
    {:style {:padding   20 :padding-top 40 :font-size 20
             :flex-wrap :nowrap}}
    (map (comp lyric-line parse-lyric-line) text)]])