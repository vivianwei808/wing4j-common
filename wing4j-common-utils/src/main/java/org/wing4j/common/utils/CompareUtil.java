package org.wing4j.common.utils;

import java.util.List;

/**
 * Created by wing4j on 2017/5/18.
 */
public class CompareUtil {
    static enum Type{
        SAME,
        DIFF
    }
    static class StringWrapper{
        Type type;
        String value;

        public StringWrapper(Type type, String value) {
            this.type = type;
            this.value = value;
        }

        public Type getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 比较字符串，输出比较结果<br>
     *     构建基于Needleman/Wunsch算法的矩阵
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 比较结果
     */
    public static List<StringWrapper>[] compare(String str1, String str2){

        return null;
    }




    /*全局变量用于回溯是的指针*/
    static int l = 0;

    public static void main(String[] args) {
        /*比对的两列字符串*/
        String t = "GCGCAATG";
        String p = "GCCCTAGCG";
        /*创建H矩阵用于打分，成为打分矩阵，创建D矩阵用于回溯，成为指针矩阵或者方向矩阵*/
        int tlen = t.length();
        int plen = p.length();
        int[][] h = new int[tlen + 1][plen + 1];
        int[][] d = new int[tlen + 1][plen + 1];
        /*初始化矩阵，第一列或行为deletion后者insert,扣分2*/
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < plen + 1; j++) {
                h[i][j] = -2 * j;
                d[i][j] = 3;
            }
        }
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < tlen + 1; i++) {
                h[i][j] = -2 * i;
                d[i][j] = 1;
            }
        }
        /*动态规划用于打分*/
        for (int i = 1; i < tlen + 1; i++) {
            for (int j = 1; j < plen + 1; j++) {
                /*分值：mismatch（失配）-1，deletion(缺失)/inserting（插入）-2，match（匹配）1，*/
                int s1 = -2, s2 = 0, s3 = -2;
                if (t.charAt(i - 1) == p.charAt(j - 1)) {
                    s2 = 1;
                } else {
                    s2 = -1;
                }
                h[i][j] = maximum(h[i - 1][j] + s1, h[i - 1][j - 1] + s2, h[i][j - 1] + s3);
                d[i][j] = l;
            }
        }
        /*输出打分矩阵*/
        System.out.println("score matrix:");
        for (int i = 0; i < tlen + 1; i++) {
            for (int j = 0; j < plen + 1; j++) {
                System.out.printf("%4d", h[i][j]);
                if (j != 0 && j % plen == 0) {
                    System.out.println();
                }
            }
        }
        /*输出索引矩阵*/
        System.out.println("index matrix:");
        for (int i = 0; i < tlen + 1; i++) {
            for (int j = 0; j < plen + 1; j++) {
                System.out.print(d[i][j] + " ");
                if (j != 0 && j % plen == 0) {
                    System.out.println();
                }
            }
        }
        /*输出结果*/
        System.out.print("Target sequence:");
        String result = get_back(t, p, d);
        for (int i = 0; i < result.length(); i++) {
            System.out.print(result.charAt(i) + " ");
        }
        System.out.println();
        System.out.print("Source sequence:");
        for (int i = 0; i < p.length(); i++) {
            System.out.print(p.charAt(i) + " ");
        }
    }

    /*求最大值的方法*/
    public static int maximum(int a, int b, int c) {
        int max = a;
        l = 1;
        if (a < b) {
            max = b;
            l = 2;
            if (b < c) {
                max = c;
                l = 3;
            }
        } else if (a < c) {
            max = c;
            l = 3;
        }
        if (max == a && max == b) {
            l = 4;
        } else if (max == a && max == c) {
            l = 5;
        } else if (max == b && max == c) {
            l = 6;
        }
        if (max == a && max == b && max == c) {
            l = 7;
        }
        return max;
    }

    /*回溯方法*/
    public static String get_back(String t, String p, int[][] d) {
        int i = t.length();
        int j = p.length();
        StringBuffer sb = new StringBuffer();
        while (i >= 0 && j > 0) {
            int start = d[i][j];
            switch (start) {
                case 1:
                    sb.insert(0, t.charAt(i - 1));
                    i = i - 1;
                    break;
                case 2:
                    sb.insert(0, t.charAt(i - 1));
                    i = i - 1;
                    j = j - 1;
                    break;
                case 3:
                    sb.insert(0, '-');
                    j = j - 1;
                    break;
                case 4:
                    sb.insert(0, t.charAt(i - 1));
                    i = i - 1;
                    j = j - 1;
                    break;
                case 5:
                    sb.insert(0, t.charAt(i - 1));
                    i = i - 1;
                    break;
                case 6:
                    sb.insert(0, t.charAt(i - 1));
                    i = i - 1;
                    j = j - 1;
                    break;
                case 7:
                    sb.insert(0, t.charAt(i - 1));
                    i = i - 1;
                    j = j - 1;
                    break;
            }
        }
        String result = sb.toString();
        return result;
    }
}
