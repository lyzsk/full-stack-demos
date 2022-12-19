package temp;

/**
 * append any character to s, cost 5 points
 * copy and append any substring of s that ends at the current rightmost character, cost 5 points
 * abhihibhihi -> 30
 * abhi -> abhihi -> abhihibhihi
 *
 * @author sichu
 * @date 2022/11/24
 **/
public class StringConstruction {
    public static void main(String[] args) {
        // System.out.println(String.valueOf('a'));
        String str1 = "abhihibhihi";
        String str2 = "aaabc";
        String str3 = "aaabbbbc";
        String str4 = "bacbacacb";
        String str5 = "aabaacaba";
        // 30, 25, 35, 42, 26
        // StringConstruction stringConstruction = new StringConstruction();
        // System.out.println(stringConstruction.minimumCost(str1));
        // System.out.println(stringConstruction.minimumCost(str2));
        // System.out.println(stringConstruction.minimumCost(str3));
        System.out.println(minimumCost(5, 5, str1));
        System.out.println(minimumCost(5, 5, str2));
        System.out.println(minimumCost(5, 5, str3));
        System.out.println(minimumCost(8, 9, str4));
        System.out.println(minimumCost(4, 5, str5));
    }

    private static int minimumCost(int a, int b, String s) {
        int res = 0;
        StringBuilder sb = new StringBuilder();
        while (sb.length() < s.length()) {
            String maxSubstring = findMaxSubstring(s, sb);
            if (maxSubstring != null) {
                sb.append(maxSubstring);
                res += b;
            } else {
                sb.append(s.charAt(sb.length()));
                res += a;
            }
        }
        return res;
    }

    private static String findMaxSubstring(String s, StringBuilder sb) {
        int sLen = s.length();
        int sbLen = sb.length();
        for (int i = sLen - sbLen; i > 1; i--) {
            String target = s.substring(sbLen, sbLen + i);
            if (sb.indexOf(target) >= 0) {
                return target;
            }
        }
        return null;
    }
}
