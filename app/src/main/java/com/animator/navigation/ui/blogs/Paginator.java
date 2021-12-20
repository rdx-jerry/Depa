package com.animator.navigation.ui.blogs;

import java.util.ArrayList;

public class Paginator {
    public static int TOTAL_NUM_ITEMS;
    public static int ITEMS_PER_PAGE;
    public static int ITEMS_REMAINING;
    public static int LAST_PAGE;
    ArrayList<Blogs> pageData;
//    BlogsFragment blogsFragment;
//    ArrayList<String> pageData;
    Blogs blogs;
    public ArrayList<Blogs> generatePage(int currentPage)
    {
        pageData=new ArrayList<>();
        TOTAL_NUM_ITEMS = pageData.size();
        ITEMS_PER_PAGE = 2;
        ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;

        int startItem = currentPage * ITEMS_PER_PAGE + 1;
        int numOfData = ITEMS_PER_PAGE;

        if (currentPage == LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i = startItem; i < startItem + ITEMS_REMAINING; i++)
            {
                pageData.indexOf(i);
            }
        }else
        {
            for (int i = startItem; i < startItem + numOfData; i++)
            {
                pageData.indexOf(i);
            }
        }
        return pageData;
    }
}
