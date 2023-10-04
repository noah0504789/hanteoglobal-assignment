package com.hanteo.coin;

public class Coin {
    public static int solution(int sum, int[] coins) {
        int[] dp = new int[sum + 1];
        dp[0] = 1;

        for (int coin : coins) {
            for (int j = coin; j <= sum; j++) {
                dp[j] += dp[j - coin];
            }
        }

        return dp[sum];
    }

    public static void main(String[] args) {
        System.out.println(solution(4, new int[] {1, 2, 3}));
        System.out.println(solution(10, new int[] {2, 5, 3, 6}));
    }
}
