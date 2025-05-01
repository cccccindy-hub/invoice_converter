<template>
  <el-card class="chart-card" :body-style="{ padding: '20px' }">
    <template #header>
      <div class="card-header">
        <h3>{{ title }}</h3>
      </div>
    </template>

    <div v-loading="loading" class="chart-wrapper">
      <div :id="chartId" class="echart" style="height: 450px"></div>
    </div>
  </el-card>
</template>

<script setup>
import { onMounted, watch, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  title: String,
  chartId: String,
  chartData: {
    type: Object,
    default: () => ({
      labels: [],
      qualified: [],
      converted: [],
      series1: [],
      series2: [],
      other: []
    })
  },
  loading: Boolean
})

let chartInstance = null

const getSeriesConfig = () => {
  const category = getChartCategory()

  // 定义系列名称映射
  const seriesNames = {
    HR: {
      series1: 'Converted EOR',
      series2: 'Converted Payroll'
    },
    FDI: {
      series1: 'Converted Recruit',
      series2: 'Converted Tax'
    }
  }
  return [
    {
      name: 'Conversion Rate',
      type: 'line',
      data: props.chartData.converted.map((v, i) =>
          (v / props.chartData.qualified[i]).toFixed(4)
      ),
      smooth: true,
      yAxisIndex: 0,
      itemStyle: {color: '#5470C6'},
      label: {
        show: true,
        position: 'top',
        formatter: ({value}) => `${(value * 100).toFixed(1)}%`,
        color: '#2c3546',
        fontSize: 12,
        borderRadius: 4,
        padding: [2, 4]
      },
      tooltip: {
        valueFormatter: value => `${(value * 100).toFixed(2)}%`
      }
    },
    {
      name: 'Qualified Leads',
      type: 'bar',
      data: props.chartData.qualified,
      yAxisIndex: 1,
      itemStyle: {color: '#91CC75'},
      label: { // 新增label配置
        show: true,
        position: 'top',
        color: '#666',
        fontSize: 12,
        formatter: ({value}) => value.toLocaleString()
      },
      tooltip: {
        valueFormatter: value => value.toLocaleString()
      }
    },
    {
      name: seriesNames[category].series1,
      type: 'bar',
      data: props.chartData.series1,
      yAxisIndex: 1,
      itemStyle: {color: '#FAC858'},
      stack: 'Converted',
      tooltip: {
        valueFormatter: value => value.toLocaleString()
      }
    },
    {
      name: seriesNames[category].series2,
      type: 'bar',
      data: props.chartData.series2,
      yAxisIndex: 1,
      itemStyle: {color: '#EE6666'},
      stack: 'Converted',
      tooltip: {
        valueFormatter: value => value.toLocaleString()
      }
    },
    {
      name: 'Converted Other',
      type: 'bar',
      data: props.chartData.other,
      yAxisIndex: 1,
      itemStyle: {color: '#73C0DE'},
      stack: 'Converted',
      label: {  // 新增标签配置
        show: true,
        position: 'top',
        formatter: (params) => {
          // 计算三个系列的总和
          const series1 = props.chartData.series1[params.dataIndex] || 0
          const series2 = props.chartData.series2[params.dataIndex] || 0
          const other = params.value || 0
          return (series2 + series1 + other).toLocaleString()
        },
        color: '#333',
        fontSize: 12
      },
      tooltip: {
        valueFormatter: value => value.toLocaleString()
      }
    }
  ]
}

const initChart = () => {
  const category = getChartCategory() // 先获取分类

  // 统一使用同一个系列名称配置
  const seriesNames = {
    HR: {
      series1: 'Converted EOR',
      series2: 'Converted Payroll'
    },
    FDI: {
      series1: 'Converted Recruit',
      series2: 'Converted Tax'
    }
  }

  const option = {
    title: {
      text: props.title,
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255,255,255,0.9)',
      borderWidth: 1,
      borderColor: '#ccc',
      textStyle: {
        color: '#333'
      }
    },
    legend: {
      data: ['Conversion Rate', 'Qualified Leads',  seriesNames[category].series1,
        seriesNames[category].series2, 'Converted Other'],
      top: 30,
      itemWidth: 20,
      itemHeight: 14
    },
    grid: {
      top: 80,
      bottom: 45,
      left: 60,
      right: 60
    },
    xAxis: {
      type: 'category',
      data: props.chartData.labels,
      axisLabel: {
        rotate: 35,
        margin: 15
      },
      axisTick: {
        alignWithLabel: true
      }
    },
    yAxis: [
      {
        type: 'value',
        name: 'Conversion Rate (%)',
        axisLabel: {
          formatter: value => `${(value * 100).toFixed(0)}%`
        },
        splitLine: {
          lineStyle: {
            type: 'dashed'
          }
        }
      },
      {
        type: 'value',
        name: 'Count',
        position: 'right',
        splitLine: { show: false },
        axisLabel: {
          formatter: value => value.toLocaleString()
        }
      }
    ],
    series: getSeriesConfig()
  }

  if (!chartInstance) {
    chartInstance = echarts.init(document.getElementById(props.chartId))
  }
  chartInstance.setOption(option, true)
}

// ChartBox组件中
const getChartCategory = () => {
  const hrType = ['HROne', 'NNRoad']
  const fdiType = ['FDI China', 'Top FDI']

  if (hrType.includes(props.chartId)) return 'HR'
  if (fdiType.includes(props.chartId)) return 'FDI'
  return 'default'
}


// 监听数据变化
watch(() => props.chartData, () => {
  initChart()
}, { deep: true })

// 窗口resize处理
const handleResize = () => {
  chartInstance?.resize()
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
  initChart()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
  chartInstance = null
})
</script>

<style scoped>
.chart-card {
  margin-bottom: 20px;
  transition: transform 0.3s ease;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.chart-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.echart {
  width: 100%;
  min-height: 500px;
}
</style>
