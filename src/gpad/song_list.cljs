(ns gpad.song-list
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [react-native :as rrn]
            [clojure.string :as str]
            [re-frame.core :as rf]))

(defn separator []
  [rn/view {:style {:borderBottomColor :gray
                    :borderBottomWidth 1}}])

(defn item [item]
  (let [item (.-item item)]
    (js/console.log item)
    (r/as-element
      [rn/view
       [rn/text
        {:style   {:font-size     23 :margin-left 10 :margin-top 5
                   :margin-bottom 5 :margin-right 10}
         :onPress #(rf/dispatch [:show-song (.-id item)])}
        (.-title item)]
       [separator]])))

(defn song-list [data]
  [rn/virtualized-list
   {:renderItem         item
    :data               []
    :get-item           (fn [d index]
                          (-> (get data index)
                              (assoc :id index)
                              (clj->js)))
    :get-item-count     (fn [d] (count data))
    :key-extractor      (fn [item] (.-id item))
    :initialNumToRender 11}])
