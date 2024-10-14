<script setup lang="ts">
import { Ref, ref } from 'vue';
import { rules } from '../utils/validators';
import request from '../utils/request';
import { useAlertStore } from '../utils/store';

const searchText: Ref<string> = ref('');
const searching: Ref<boolean> = ref(false);

const violations: Ref<{ name: string, violations: string[]} | null> = ref(null);

async function getViolationsByPersonId() {
  searching.value = true;
  try {
    let response = await request.get('staff/violations', { params: { personId: searchText.value } });
    violations.value = response.data.data;

    useAlertStore().showMessage('success', '查询成功');
  } catch (error) {
    console.error(error);
    violations.value = null;
  } finally {
    searching.value = false;
  }
}
</script>

<template>
  <v-card>
    <v-card-text>
      <v-toolbar height=80>
        <v-text-field label="身份证号" style="margin-left: 10px;" max-width="300px" clearable :hide-details="true" v-model="searchText"></v-text-field>
        <v-btn prepend-icon="mdi-magnify" color="primary" @click="getViolationsByPersonId()" :disabled="rules.personId(searchText) !== true" :loading="searching">查询</v-btn>
      </v-toolbar>
      <v-card v-if="violations != null" style="text-align: left; margin-top: 20px;">
        <v-card-title>查询到员工 {{ violations.name }} 的违规记录</v-card-title>
        <v-card-text>
          <v-list>
            <v-list-item v-for="violation in violations.violations" prepend-icon="mdi-circle-medium">{{ violation }}</v-list-item>
          </v-list>
        </v-card-text>
      </v-card>
    </v-card-text>
  </v-card>
</template>