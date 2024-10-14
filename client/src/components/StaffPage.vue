<script setup lang="ts">
import { onMounted, Ref, ref } from 'vue';
import { rules } from '../utils/validators';
import request from '../utils/request';
import { useAlertStore } from '../utils/store';
import { SERVER } from '../config';

interface Staff {
  id: number;
  name: string;
  image: string;
  age: number;
  personId: string;
  experience: string;
  physicalCondition: string;
  appendix: string;
}

function newStaff(): Staff {
  return { id: 0, name: '', image: '', age: 0, personId: '', experience: '', physicalCondition: '', appendix: '' };
}

const showTable: Ref<boolean> = ref(false);
const readonly: Ref<boolean> = ref(true);
const staffInTable: Ref<Staff> = ref(newStaff());
const searching: Ref<boolean> = ref(false);
const showEditButton: Ref<boolean> = ref(false);
const showSaveButton: Ref<boolean> = ref(false);
const showDeleteButton: Ref<boolean> = ref(false);
const isValid: Ref<boolean> = ref(false);
const saving: Ref<boolean> = ref(false);
const deleting: Ref<boolean> = ref(false);
const uploading: Ref<boolean> = ref(false);
const imageURL: Ref<string> = ref('');
const imageFile: Ref<File | null> = ref(null);

const searchText: Ref<string> = ref('');

async function getStaffByPersonId() {
  searching.value = true;
  try {
    let response = await request.get('staff', { params: { personId: searchText.value } });
    staffInTable.value = response.data.data;
    imageURL.value = `${SERVER}image/staff/${staffInTable.value.image}`;
    showTable.value = true;
    readonly.value = true;
    showDeleteButton.value = true;
    showEditButton.value = true;
    useAlertStore().showMessage('success', '查询成功');
  } catch (error) {
    console.error(error);
  } finally {
    searching.value = false;
  }
}

function editStaff() {
  readonly.value = false;
  showEditButton.value = false;
  showSaveButton.value = true;
}

function repaceNullByEmptyString(staff: Staff) {
  if (staff.experience === null)
    staff.experience = '';
  if (staff.physicalCondition === null)
    staff.physicalCondition = '';
  if (staff.appendix === null)
    staff.appendix = '';
}

async function saveStaff() {
  saving.value = true;
  repaceNullByEmptyString(staffInTable.value);
  try {
    await uploadImage();
  } catch (error) {
    console.error(error);
    await deleteImage();
    saving.value = false;
    return;
  }
  try {
    console.log("staffInTable: " + JSON.stringify(staffInTable.value));
    if (staffInTable.value.id === 0) {
      await request.put('staff', staffInTable.value);
    } else {
      await request.post('staff/update', staffInTable.value);
    }
    useAlertStore().showMessage('success', '保存成功');
    showSaveButton.value = false;
    showEditButton.value = true;
    readonly.value = true;
  } catch (error) {
    console.error(error);
    await deleteImage();
    saving.value = false;
  } finally {
    saving.value = false;
  }
}

async function createStaff() {
  staffInTable.value = newStaff();
  showTable.value = true;
  readonly.value = false;
  showEditButton.value = false;
  showSaveButton.value = true;
  showDeleteButton.value = false;
  imageURL.value = '';
}

async function deleteStaff() {
  deleting.value = true;
  await deleteImage();
  try {
    await request.delete('staff', { params: { id: staffInTable.value.id } });
    useAlertStore().showMessage('success', '删除成功');
    showTable.value = false;
    searchText.value = '';
    showDeleteButton.value = false;
    showEditButton.value = false;
    showSaveButton.value = false;
  } catch (error) {
    console.error(error);
  } finally {
    deleting.value = false;
  }
}

async function deleteImage() {
  if (!!staffInTable.value.image) {
    await request.delete(`image/staff/${staffInTable.value.image}`);
    staffInTable.value.image = '';
  } else {
    console.log("No image to delete");
  }
}

async function uploadImage() {
  if (imageFile.value === null) {
    return;
  }
  uploading.value = true;
  try {
    await deleteImage();

    let formData = new FormData();
    formData.append('file', imageFile.value!);
    let response = await request.put('image/upload/staff', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
    staffInTable.value.image = response.data.data;
    console.log("image: " + staffInTable.value.image);
  } catch (error) {
    console.error(error);
  } finally {
    uploading.value = false;
  }
}

async function selectImage() {
  let file = await new Promise<File>((resolve) => {
    let input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.onchange = () => {
      resolve(input.files![0]);
    };
    input.click();
  });
  imageURL.value = URL.createObjectURL(file);
  imageFile.value = file;
}

onMounted(() => {
  showTable.value = false;
  searchText.value = '';
  showDeleteButton.value = false;
  showEditButton.value = false;
  showSaveButton.value = false;
})
</script>

<template>
  <v-card>
    <v-card-text>
      <v-toolbar height=80>
        <v-text-field label="身份证号" style="margin-left: 10px;" max-width="300px" clearable :hide-details="true" v-model="searchText"></v-text-field>
        <v-btn prepend-icon="mdi-magnify" color="primary" @click="getStaffByPersonId()" :disabled="rules.personId(searchText) !== true" :loading="searching">查询</v-btn>
        <v-spacer></v-spacer>
        <v-btn prepend-icon="mdi-plus" color="primary" @click="createStaff()">新建</v-btn>
      </v-toolbar>
      <v-form v-if="showTable" v-model="isValid">
        <table class="h-center" border="1" cellspacing="0">
          <tbody>
            <tr>
              <td>
                <v-text-field label="姓名" width="250px" :rules="[rules.required]" density="compact" :readonly="readonly" v-model="staffInTable.name" :hide-details="readonly ? true : 'auto'"></v-text-field>
              </td>
              <td>
                <v-text-field label="年龄" width="250px" :rules="[rules.required]" density="compact" :readonly="readonly" v-model.number="staffInTable.age" :hide-details="readonly ? true : 'auto'"></v-text-field>
              </td>
              <td rowspan="2">
                <v-img v-if="!!imageURL" :src="imageURL" width="150px" height="225px" @click="readonly ? true : selectImage()"></v-img>
                <v-btn v-else width="150px" height="225px" :disabled="readonly" @click="selectImage">上传照片</v-btn>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <v-text-field label="身份证号" width="500px" :rules="[rules.required, rules.personId]" density="compact" :readonly="readonly" v-model="staffInTable.personId" :hide-details="readonly ? true : 'auto'"></v-text-field>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="个人经历" width="650px" :readonly="readonly" v-model="staffInTable.experience" :hide-details="readonly ? true : 'auto'"></v-textarea>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="身体状况" width="650px" :readonly="readonly" v-model="staffInTable.physicalCondition" :hide-details="readonly ? true : 'auto'"></v-textarea>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="备注" width="650px" :readonly="readonly" v-model="staffInTable.appendix" :hide-details="readonly ? true : 'auto'"></v-textarea>
              </td>
            </tr>
          </tbody>
        </table>
        <v-toolbar style="width: 650px; background-color: white;" class="h-center">
          <v-spacer></v-spacer>
          <v-btn v-if="showDeleteButton" color="error" @click="deleteStaff">删除</v-btn>
          <v-btn v-if="showEditButton" @click="editStaff">编辑</v-btn>
          <v-btn v-if="showSaveButton" @click="saveStaff" :loading="saving">保存</v-btn>
        </v-toolbar>
      </v-form>
    </v-card-text>
  </v-card>
</template>