(ns gpad.core
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [react-native :as rrn]
            [clojure.string :as str]
            [gpad.viewer :as viewer]))

(comment
  (def flat-list (r/adapt-react-class rrn/FlatList)))

(defn log [d] (js/console.log d))

(log "start")
(log rrn/Separator)

(defn separator []
  [rn/view {:style {:borderBottomColor "black"
                    :borderBottomWidth 2}}])

(defn item [item]
  (let [item (.-item item)]
    (r/as-element
      [rn/view
       [rn/text {:style {:font-size 40}} (.-title item)]
       [separator]])))

(def lyric
  ["[Asus4]Here's a thought for every man who tries to understand"
   "what is in his [G]hands  (what's in his hands)"
   "He [Asus4]walks along the open road of love and life"
   "surviving if he [G]can   (only if he can)"
   "-"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]Chained to all the [D]places that he [C]never wished to [G5]stay"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]As he faced the [D]sun he [C]cast no shadow"
   ""
   "[Am][C][D][E]"
   "And [D]after all,[Asus7]"])

(defn hello []
  [rn/safe-area-view {:style {:flex 1}} #_{:style {:flex 1 :align-items "center" :justify-content "center"}}

   [viewer/viewer lyric]
   #_[rn/flat-list {:data       [{:title "test" :id 1} {:title "test3" :id 2}]
                    :renderItem item}]
   #_[rn/text {:style {:font-size 50}} "Hello Krell!!!"]])

(defn ^:export -main [& args]
  (r/as-element [hello]))