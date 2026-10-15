# Smart Hybrid Page Indicator

Transition from a simple numerical list to a smart pagination system that uses ellipses (...) when the page count exceeds 10. This maintains information clarity while ensuring visual stability.

## Proposed Changes

### [Component] UI Layer
#### [MODIFY] [DrawerScreen.kt](file:///C:/My_Programming_Projects/Android Projects/Categorysym/app/src/main/java/com/example/categorysym/ui/components/screens/DrawerScreen.kt)
- **Update `PageIndicator` logic:**
    - Define a threshold: `MAX_VISIBLE_PAGES = 10`.
    - **Condition A (pageCount <= 10):** Keep the current behavior (render all numbers).
    - **Condition B (pageCount > 10):**
        - Always render **Index 0** (Newspaper Icon).
        - Always render **Index 1** (Page 1).
        - Always render **Index pageCount - 2** (Last Page Number).
        - Always render **Index pageCount - 1** (Lock Icon).
        - **Middle Logic:**
            - If `currentPage` is near the start (<= 3): Show `1, 2, 3, ...`.
            - If `currentPage` is near the end (>= pageCount - 4): Show `..., N-2, N-1, N`.
            - If `currentPage` is in the middle: Show `1, ..., Current, ..., N`.
    - **Styling:**
        - Ensure the `...` (Ellipsis) is styled consistently with inactive numbers.
        - Maintain current active/inactive animations for icons and numbers.

## User Experience Impact
- **Clarity:** Users always know their exact page number and the total scope of their launcher.
- **Visual Integrity:** The bottom bar never overflows, even if the user has 50+ pages.
- **Consistency:** The transition at the 10-page mark feels like a deliberate design choice rather than a technical limitation.

## Verification Plan
1.  **Low Count:** Verify pages 1-10 show as `News 1 2 3 4 5 6 7 8 Lock`.
2.  **High Count (e.g., 20):**
    - Swipe to page 1: `News 1 2 3 ... 18 Lock`.
    - Swipe to page 10: `News 1 ... 10 ... 18 Lock`.
    - Swipe to page 18: `News 1 ... 16 17 18 Lock`.
3.  **Interactivity:** Verify that clicking or swiping correctly updates the abbreviated view in real-time.
