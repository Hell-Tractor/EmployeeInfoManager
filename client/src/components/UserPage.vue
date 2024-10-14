<script setup lang="ts">
import { Ref, ref } from 'vue';
import request from '../utils/request';
import { useAlertStore } from '../utils/store';
import UserDialog from './dialogs/UserDialog.vue';

export interface User {
  id: number;
  username: string;
  level: string;
  departName: string;
}

const pageSize: Ref<number> = ref(10);
const headers: { title: string, key: string | undefined, align: 'center' | 'start' | 'end' | undefined, sortable: boolean, value: string | undefined }[] = [
  { title: 'ID', key: 'id', value: undefined, align: "center", sortable: false },
  { title: '用户名', key: undefined, value: 'username', align: "center", sortable: false },
  { title: '级别', key: undefined, value: 'level', align: "center", sortable: false },
  { title: '部门', key: undefined, value: 'departName', align: "center", sortable: false },
  { title: '操作', key: undefined, value: 'actions', align: "center", sortable: false },
]
const items: Ref<User[]> = ref([]);
const totalItems: Ref<number> = ref(0);
const loading: Ref<boolean> = ref(false);

function loadItems({ page, pageSize, sortBy }: { page: number, pageSize: number, sortBy: string }) {
  sortBy;
  loading.value = true;
  request.get('user', { params: { page, pageSize } }).then((response) => {
    items.value = response.data.data.list;
    totalItems.value = response.data.data.total;
  }).catch((error) => {
    console.error(error);
  }).finally(() => {
    loading.value = false;
  });
}

function deleteItem(item: User) {
  console.log(item);
  request.delete(`user/delete/force`, { params: { 'userId': item.id } }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '删除成功');
  }).catch((error) => {
    console.error(error);
  });
}

function addItem({ username, password, departId }: { username: string, password: string, departId: number }) {
  request.post('user/create', { 'username': username, 'password': password, 'departId': departId }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '添加成功');
  }).catch((error) => {
    console.error(error);
  });
}
</script>

<template>
  <v-data-table-server
    v-model:items-per-page="pageSize"
    :headers="headers"
    :items="items"
    :items-length="totalItems"
    :loading="loading"
    @update:options="loadItems"
  >
    <template v-slot:top>
      <v-toolbar>
        <v-spacer></v-spacer>
        <UserDialog @success="addItem" :init="null">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props" width="80px" prepend-icon="mdi-plus">添加</v-btn>
          </template>"
        </UserDialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon size="small" @click="deleteItem(item)">mdi-delete</v-icon>
    </template>
  </v-data-table-server>
</template>