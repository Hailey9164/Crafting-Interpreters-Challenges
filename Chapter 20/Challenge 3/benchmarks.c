#include <stdio.h>
#include <string.h>
#include <time.h>

#include "common.h"
#include "memory.h"
#include "object.h"
#include "table.h"
#include "value.h"
#include "vm.h"

#define NUM_KEYS 100000

static void benchmarkInsertion() {
  printf("\n=== Insertion Benchmark ===\n");

  Table table;
  initTable(&table);

  clock_t start = clock();

  for (int i = 0; i < NUM_KEYS; i++) {
    char buffer[32];
    sprintf(buffer, "key%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));
    tableSet(&table, OBJ_VAL(key), NUMBER_VAL(i));
  }

  clock_t end = clock();

  printf("Inserted %d keys in %.3f seconds\n",
         NUM_KEYS,
         (double)(end - start) / CLOCKS_PER_SEC);

  freeTable(&table);
}

static void benchmarkSuccessfulLookups() {
  printf("\n=== Successful Lookup Benchmark ===\n");

  Table table;
  initTable(&table);

  for (int i = 0; i < NUM_KEYS; i++) {
    char buffer[32];
    sprintf(buffer, "key%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));
    tableSet(&table, OBJ_VAL(key), NUMBER_VAL(i));
  }

  Value value;
  clock_t start = clock();

  for (int i = 0; i < NUM_KEYS; i++) {
    char buffer[32];
    sprintf(buffer, "key%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));
    tableGet(&table, OBJ_VAL(key), &value);
  }

  clock_t end = clock();

  printf("Looked up %d existing keys in %.3f seconds\n",
         NUM_KEYS,
         (double)(end - start) / CLOCKS_PER_SEC);

  freeTable(&table);
}

static void benchmarkFailedLookups() {
  printf("\n=== Failed Lookup Benchmark ===\n");

  Table table;
  initTable(&table);

  for (int i = 0; i < NUM_KEYS; i++) {
    char buffer[32];
    sprintf(buffer, "key%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));
    tableSet(&table, OBJ_VAL(key), NUMBER_VAL(i));
  }

  Value value;

  clock_t start = clock();

  for (int i = NUM_KEYS; i < NUM_KEYS * 2; i++) {
    char buffer[32];
    sprintf(buffer, "missing%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));
    tableGet(&table, OBJ_VAL(key), &value);
  }

  clock_t end = clock();

  printf("Attempted %d failed lookups in %.3f seconds\n",
         NUM_KEYS,
         (double)(end - start) / CLOCKS_PER_SEC);

  freeTable(&table);
}

static void benchmarkInsertDelete() {
  printf("\n=== Insert/Delete Benchmark ===\n");

  Table table;
  initTable(&table);

  clock_t start = clock();

  for (int i = 0; i < NUM_KEYS; i++) {
    char buffer[32];
    sprintf(buffer, "key%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));

    tableSet(&table, OBJ_VAL(key), NUMBER_VAL(i));
    tableDelete(&table, OBJ_VAL(key));
  }

  clock_t end = clock();

  printf("Performed %d insert/delete pairs in %.3f seconds\n",
         NUM_KEYS,
         (double)(end - start) / CLOCKS_PER_SEC);

  freeTable(&table);
}

static void benchmarkHighLoadFactor() {
  printf("\n=== High Load Factor Benchmark ===\n");

  Table table;
  initTable(&table);

  int count = 500000;

  clock_t start = clock();

  for (int i = 0; i < count; i++) {
    char buffer[32];
    sprintf(buffer, "key%d", i);

    ObjString* key = copyString(buffer, (int)strlen(buffer));
    tableSet(&table, OBJ_VAL(key), NUMBER_VAL(i));
  }

  clock_t end = clock();

  printf("Inserted %d keys in %.3f seconds\n",
         count,
         (double)(end - start) / CLOCKS_PER_SEC);

  freeTable(&table);
}

int main() {
  initVM();

  benchmarkInsertion();
  benchmarkSuccessfulLookups();
  benchmarkFailedLookups();
  benchmarkInsertDelete();
  benchmarkHighLoadFactor();

  freeVM();

  return 0;
}