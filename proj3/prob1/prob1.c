#include <omp.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

#define END_NUM 200000

bool isPrime(int x);

int main(int argc, char* argv[])
{	
	int numType = atoi(argv[1]);
	int numThread = atoi(argv[2]);

	omp_set_num_threads(numThread);

	int i;
	int sum = 0;
	double start_time, end_time;
	start_time = omp_get_wtime();

	printf("Before calc : sum = %d\n", sum);

	if (numType == 1) {
		#pragma omp parallel for schedule(static) reduction(+:sum)
		for (i = 1; i <= END_NUM; i++) {
			if (isPrime(i))
				sum += 1;
		}
	}
	else if (numType == 2) {
		#pragma omp parallel for schedule(dynamic) reduction(+:sum)
		for (i = 1; i <= END_NUM; i++) {
			if (isPrime(i))
				sum += 1;
		}
	}
	else if (numType == 3) {
		#pragma omp parallel for schedule(static, 10) reduction(+:sum)
		for (i = 1; i <= END_NUM; i++) {
			if (isPrime(i))
				sum += 1;
		}
	}
	else if (numType == 4) {
		#pragma omp parallel for schedule(dynamic, 10) reduction(+:sum)
		for (i = 1; i <= END_NUM; i++) {
			if (isPrime(i))
				sum += 1;
		}
	}

	end_time = omp_get_wtime();

	printf("After calc : sum = %d\n", sum);
	printf("time elapsed: %lfs\n", end_time - start_time);

	return 0;
}

bool isPrime(int x) {
	int i;
	if (x <= 1) return false;
	for (i = 2; i < x; i++)
		if (x % i == 0) return false;
	return true;
}