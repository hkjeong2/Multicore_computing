#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

long num_steps = 10000000;
double step;

int main(int argc, char* argv[])
{
	int numType = atoi(argv[1]);
	int numSize = atoi(argv[2]);
	int numThread = atoi(argv[3]);

	omp_set_num_threads(numThread);

	long i; double x, pi, sum = 0.0;
	double start_time, end_time;

	start_time = omp_get_wtime();
	step = 1.0 / (double)num_steps;

	if (numType == 1) {
		#pragma omp parallel for schedule(static, numSize) reduction(+:sum) private(x)
		for (i = 0; i < num_steps; i++) {
			x = (i + 0.5) * step;
			sum = sum + 4.0 / (1.0 + x * x);
		}
	}
	else if (numType == 2) {
		#pragma omp parallel for schedule(dynamic, numSize) reduction(+:sum) private(x)
		for (i = 0; i < num_steps; i++) {
			x = (i + 0.5) * step;
			sum = sum + 4.0 / (1.0 + x * x);
		}
	}
	else if (numType == 3) {
		#pragma omp parallel for schedule(guided, numSize) reduction(+:sum) private(x)
		for (i = 0; i < num_steps; i++) {
			x = (i + 0.5) * step;
			sum = sum + 4.0 / (1.0 + x * x);
		}
	}

	pi = step * sum;

	end_time = omp_get_wtime();

	double timeDiff = end_time - start_time;
	printf("Execution Time : %lfms\n", timeDiff);
	printf("pi=%.24lf\n", pi);

	return 0;
}