
#include <iostream>
// #include <cstdio>
#include <thread>
// #include <ctime>
using namespace std;

const int NUMBER_OF_THREADS = 1;

const int NUMBER_OF_ELEMENTS = 2000000000;
// const int NUMBER_OF_ELEMENTS = 5242880;
// const int NUMBER_OF_ELEMENTS = 1572864;

const int step = NUMBER_OF_ELEMENTS / NUMBER_OF_THREADS;

long long sum;
long long sumArr[NUMBER_OF_THREADS] = {1};
// ,1,1,1};

void calcSum( int start, int limit )
{
	std::thread::id this_id = std::this_thread::get_id();

	auto t_start = std::chrono::high_resolution_clock::now();

	int sumIdx = start / step;
    for( int i = start; i <= limit; i++ )
        sumArr[sumIdx] *= i;

	auto t_end = std::chrono::high_resolution_clock::now();
	std::cout << "thread " << this_id << " done in: " << (std::chrono::duration<double, std::milli>(t_end-t_start).count()) << " \n";
}

int main()
{
    thread t[NUMBER_OF_THREADS];

	printf("%lu\n", sizeof(int));

	std::thread::id this_id = std::this_thread::get_id();
	auto t_start = std::chrono::high_resolution_clock::now();

    for( int i = 0; i < NUMBER_OF_THREADS; i++ )
        t[i] = thread(calcSum, i*step + 1, (i+1)*step);

    for( int i = 0; i < NUMBER_OF_THREADS; i++ )
        t[i].join();

//    for( int i = 0; i < NUMBER_OF_THREADS; i++ )
//        sum += sumArr[i];
//    printf("Sum= %lld\n", sum);

	auto t_end = std::chrono::high_resolution_clock::now();
	std::cout << "main thread " << this_id << " done in: " << (std::chrono::duration<double, std::milli>(t_end-t_start).count()) << " \n";

    // printf("Time spent: %.5f\n", (float)spentTime/CLOCKS_PER_SEC);
    printf("Number of threads: %d\n", NUMBER_OF_THREADS);
    printf("Number of elements: %d\n", NUMBER_OF_ELEMENTS);

    return 0;
}
