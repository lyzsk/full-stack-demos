import java.util.Arrays;

/**
 * @author sichu
 * @date 2022/11/23
 **/
public class MergeSort {
    private static void mergeSort(int[] arr,int left, int right,int[] temp) {
        if (left < right) {
            int mid = left + ((right - left) >> 1);
            mergeSort(arr, left, mid, temp);
            mergeSort(arr, mid + 1, right, temp);
            merge(arr, left, mid, right, temp);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int l = left;
        int r = mid + 1;
        int ptr = 0;
        while (l <= mid && r <= right) {
            if (arr[l] <= arr[r]) {
                temp[ptr++] = arr[l++];
            } else {
                temp[ptr++] = arr[r++];
            }
        }
        // 左部分剩余的元素
        while (l <= mid) {
            temp[ptr++] = arr[l++];
        }
        // 右部分剩余的元素
        while (r <= right) {
            temp[ptr++] = arr[r++];
        }
        // ptr 置 0, 拷贝回原数组
        ptr = 0;
        while (left <= right) {
            arr[left++] = temp[ptr++];
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] {9,8,7,6,5,4,3,2,1};
        int[] temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, temp);
        System.out.println(Arrays.toString(arr));
    }
}
