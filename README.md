# Music Advisor
* Simple connect with Spotify API and data processing from it. In this project work with Spotify’s API, get acquainted with Java Generics, apply abstract class and using OAuth 2.0. Is an implementation of the [Music Advisor](https://hyperskill.org/projects/62) project from [hyperskill.org](https://www.jetbrains.com/ru-ru/academy/).

## About
* Launching and using the program:
1. Create Spotify apps. Take client ID and client secret code in your apps. [Help](https://developer.spotify.com/documentation/general/guides/authorization/app-settings/).
2. Set your client ID and client secret code in `config.properties` file in `uri.client` and `uri.client_secret` respectively (codes taken randomly):
   ```
   uri.client = 336ed58516183a3738241acfa1e75691
   uri.client_secret = 7b8bcaeda9a75822331ba1b144362fea
   ```
3. After starting the program, you need to authenticate the user using the `auth` command.
4. Keywords using in app:
   * `new` is used to show new songs:
      ```
      As It Was
      [Harry Styles]
      https://open.spotify.com/album/2pqdSWeJVsXAhHFuVLzuA8

      When You're Gone
      [Shawn Mendes]
      https://open.spotify.com/album/4EGVr9mSwFPoqvDMkiahJp

      Unlimited Love
      [Red Hot Chili Peppers]
      https://open.spotify.com/album/2ITVvrNiINKRiW7wA3w6w6

      Leave You Alone
      [Ella Mai]
      https://open.spotify.com/album/2kiSvbyjIZCsoSYw40josY

      Where We Started
      [Thomas Rhett]
      https://open.spotify.com/album/794M3R461sLnY042CsB3xW

      ---PAGE 1 OF 4---
      ```
   * `featured` is used to show popular playlist:
      ```
      Easy On Monday
      https://open.spotify.com/playlist/37i9dQZF1DWYlXpl3xXzDI

      Cozy Blend
      https://open.spotify.com/playlist/37i9dQZF1DXcxacyAXkQDu

      Today's Top Hits
      https://open.spotify.com/playlist/37i9dQZF1DXcBWIGoYBM5M

      Soft Rock
      https://open.spotify.com/playlist/37i9dQZF1DX6xOPeSOGone

      Totally Stress Free
      https://open.spotify.com/playlist/37i9dQZF1DWT7XSlwvR1ar

      ---PAGE 1 OF 2---
      ```
   * `categories` is used to show categories of music:
      ```
      Top Lists
      Pop
      Hip-Hop
      Mood
      Dance/Electronic
      ---PAGE 1 OF 4---
      ```
   * `playlists 'name_playlist'` is used to show the playlist with the selected name `'name_playlist'`.
      For example `playlists Top Lists`:
      ```
      Today's Top Hits
      https://open.spotify.com/playlist/37i9dQZF1DXcBWIGoYBM5M

      RapCaviar
      https://open.spotify.com/playlist/37i9dQZF1DX0XUsuxWHRQd

      mint
      https://open.spotify.com/playlist/37i9dQZF1DX4dyzvuaRJ0n

      Rock This
      https://open.spotify.com/playlist/37i9dQZF1DXcF6B6QPhFDv

      Are & Be
      https://open.spotify.com/playlist/37i9dQZF1DX4SBhb3fqCJd

      ---PAGE 1 OF 3---
      ```
   * `next` is used to view the next page.
   * `prev` is used to view the previous page.
   * `exit` is used to exit the program.