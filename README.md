# Clone Binance Home Screen

https://github.com/89hnim/jet-binance/assets/36001688/26d23c01-4c85-453b-80f0-914a6549d261

In this home screen, there are some tricky parts. Let me highlight a few things:

1. Animation:  Jetpack Compose did very well with animation api. It’s very easy to use and very smooth too.
    - Search bar
    - Navigate between tabs Coin List: Favorites, Hot, New Listings .etc.
    - Button notification when viewing feed discover.
    - Button move to top when view feed discover and is scrolling up
    - React popup: scale when hover

2. Custom view: Draw the background has 2 triangles with different color.

![a](https://github.com/89hnim/jet-binance/assets/36001688/7c065d91-537c-48c1-9743-20a1beea3855)

3. Sticky header in discover/news feed

4. Feed post:
    - Support 2 types: post with title or only body
    - Limit lines bases on post type. For example post with title has a maximum number of body lines of 3. Otherwise other posts are not limited to the number of lines.
    - Support #hashtag
    - List featured coins with horizontal scrolling
    - Reaction popup: show below or above anchor depending on the remaining space at the top. Also has animation when hover 

![image](https://github.com/89hnim/jet-binance/assets/36001688/5c2a42e7-a5d8-4ebb-aa33-47fc5bd95708)
![image](https://github.com/89hnim/jet-binance/assets/36001688/34168de4-04ba-483c-ad8e-ddbb8dc759d6)

[🚧 WIP] Custom chart using jetpack compose

Note: For sake of simplicity, this sample I passed view model down to compose components. You shouldn't do this in real-world project which may cause hard to debug, less reusable and harder to test. [Learn more](https://developer.android.com/topic/architecture/ui-layer/stateholders#business-logic)



