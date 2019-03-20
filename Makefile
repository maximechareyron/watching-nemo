export CC=gcc
export CFLAGS=-Wall -Wextra -std=c99 -g -O0
PWD=$(shell pwd)
export BUILD_DIR=$(PWD)/build


all: controller


controller: build_dir
	@make -C src/$@


build_dir:
	mkdir -p $(BUILD_DIR)



